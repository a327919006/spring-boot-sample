#!/bin/bash

zeroCommit='0000000000000000000000000000000000000000'
mergeRegex='^(Merge|merge).*'
contentRegex='^(feat|fix):\[[0-9]+(,[0-9]+)*\].*|(docs|test|ci|revert):.*'
userInfoFile='/var/opt/gitlab/gitaly/user.info'
urlSession='http://192.168.1.221:82/zentao/api-getsessionid.json'
urlLogin='http://192.168.1.221:82/zentao/user-login.json?account=xxx&password=xxx'

if [[ "$GL_PROJECT_PATH" == "ai/cnte-algo-etl" || "$GL_PROJECT_PATH" == "ai/cnte-algo-vehicle-detection"
|| "$GL_PROJECT_PATH" == "ems/commcomponents" || "$GL_PROJECT_PATH" == "test/test1" ]]; then
  while read -r oldrev newrev refname; do

    echo "[INFO]开始检查提交信息..."
    echo "[INFO]您提交的分支为：${refname#refs/heads/}"

    # Branch or tag got deleted, ignore the push
    [ "$newrev" = "$zeroCommit" ] && continue

    # Calculate range for new branch/updated branch
    [ "$oldrev" = "$zeroCommit" ] && range="$newrev" || range="$oldrev..$newrev"

    declare -A storys
    declare -A bugs

    for commit in $(git rev-list "$range" --not --all); do
      commitDate=$(git log --pretty=format:"%ci" $commit -1)
      msg=$(git log --pretty=format:"%s" $commit -1)
      flagMerge=$(echo $msg | grep -E "$mergeRegex")
      flagPush=$(echo $msg | grep -E "$contentRegex")
      if [ -z "$flagPush" ] && [ -z "$flagMerge" ]; then
        echo "[ERROR]--------------------------------------------------------"
        echo "[ERROR]提交失败：提交信息不规范，请按照规范修改提交信息后重新提交"
        echo "[ERROR]提交信息：$msg"
        echo "[ERROR]标准格式：类型:[编号]任意描述信息,类型为feat|fix|docs|test|ci"
        echo "[ERROR]提交ID：$commit"
        echo "[ERROR]提交日期：$commitDate"
        echo "[ERROR]--------------------------------------------------------"
        exit 1
      else
        if [[ ${msg:0:5} == "feat:" ]]; then
          tag=${msg%]*}
          numList=${tag#*[}
          IFS=',' read -ra numArray <<< "$numList"
          for numStr in ${numArray[*]}; do
            if [ ! -n "${storys[$numStr]}" ]
            then
              echo "[INFO]开始关联需求号："$numStr
              result=$(curl -s -c $userInfoFile $urlSession)
              result=$(curl -s -c $userInfoFile -b $userInfoFile $urlLogin)
              result=$(curl -s -b $userInfoFile http://192.168.1.221:82/zentao/story-view-$numStr.json)
              if [[ ${result:0:1} == "{" ]]; then
                echo "[INFO]需求号关联成功："$numStr
                storys[$numStr]=1
              else
                echo "[ERROR]--------------------------------------------------------"
                echo "[ERROR]提交失败：请确认需求编号[$numStr]是否存在"
                echo "[ERROR]提交信息：$msg"
                echo "[ERROR]提交ID：$commit"
                echo "[ERROR]提交日期：$commitDate"
                echo "[ERROR]--------------------------------------------------------"
                exit 1
              fi
            fi
          done
        fi


        if [[ ${msg:0:4} == "fix:" ]]; then
          tag=${msg%]*}
          numList=${tag#*[}
          IFS=',' read -ra numArray <<< "$numList"
          for numStr in ${numArray[*]}; do
            if [ ! -n "${bugs[$numStr]}" ]
            then
              echo "[INFO]开始关联BUG号："$numStr
              result=$(curl -s -c $userInfoFile $urlSession)
              result=$(curl -s -c $userInfoFile -b $userInfoFile $urlLogin)
              result=$(curl -s -b $userInfoFile http://192.168.1.221:82/zentao/bug-view-$numStr.json)
              if [[ ${result:0:1} == "{" ]]; then
                echo "[INFO]BUG号关联成功："$numStr
                bugs[$numStr]=1
              else
                echo "[ERROR]--------------------------------------------------------"
                echo "[ERROR]提交失败：请确认BUG编号[$numStr]是否存在"
                echo "[ERROR]提交信息：$msg"
                echo "[ERROR]提交ID：$commit"
                echo "[ERROR]提交日期：$commitDate"
                echo "[ERROR]--------------------------------------------------------"
                exit 1
              fi
            else
              echo "find value"
            fi
          done
        fi
      fi
    done


    #msg=$(git log --topo-order --pretty=format:"%s" -1)
    #feat=${msg% *}
    #numStr=${feat#*:}
    echo "[INFO]--------------------------------------------------------"
    echo "[INFO]本次提交关联禅道需求号："${!storys[*]}
    echo "[INFO]本次提交关联禅道BUG号："${!bugs[*]}
    echo "[INFO]--------------------------------------------------------"
  done
else
  echo "该项目无需校验"
fi
