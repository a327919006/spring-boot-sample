#!/bin/bash

zeroCommit='0000000000000000000000000000000000000000'
mergeRegex='^(Merge)(.{1,})'
contentRegex='^(feat|fix|docs|style|refactor|perf|test|build|ci|chore|revert):\[[0-9]+(,[0-9]+)*\].*'

if [ "$GL_PROJECT_PATH" = "test/test1" -o "$GL_PROJECT_PATH" = "test/test2" ]; then
  while read -r oldrev newrev refname; do

    echo "开始检查提交信息..."
    echo "您提交的分支为：$refname"

    # Branch or tag got deleted, ignore the push
    [ "$newrev" = "$zeroCommit" ] && continue

    # Calculate range for new branch/updated branch
    [ "$oldrev" = "$zeroCommit" ] && range="$newrev" || range="$oldrev..$newrev"

    declare -A storys
    declare -A bugs

    for commit in $(git rev-list "$range" --not --all); do
      user=$(git log --pretty=format:"%an" $commit -1)
      commitDate=$(git log --pretty=format:"%cd" $commit -1)
      msg=$(git log --pretty=format:"%s" $commit -1)
      flagMerge=$(echo $msg | grep -E "$mergeRegex")
      flagPush=$(echo $msg | grep -E "$contentRegex")
      if [ -z "$flagPush" ] && [ -z "$flagMerge" ]; then
        echo "[ERROR]"
        echo "[ERROR]由于这份提交日志不规范，本次提交被拒绝"
        echo "[ERROR]$commit in ${refname#refs/heads/}"
        echo "[ERROR]提交者：$user"
        echo "[ERROR]提交日期：$commitDate"
        echo "[ERROR]提交日志：$msg"
        echo "[ERROR]提交信息检查不通过!!!"
        echo "[ERROR]请按照规范修改提交日志后重新尝试提交。"pre
        echo "[ERROR]"
        exit 1
      else
        if [[ ${msg:0:5} == "feat:" ]]; then
          tag=${msg%]*}
          numList=${tag#*[}
          IFS=',' read -ra numArray <<< "$numList"
          for numStr in ${numArray[*]}; do
            if [ ! -n "${storys[$numStr]}" ]
            then
              echo "[DEBUG]开始校验需求号："$numStr
              result=$(curl -s -H "User-Agent=Mozilla/5.0" -c /var/opt/gitlab/gitaly/user.info -b /var/opt/gitlab/gitaly/user.info http://192.168.1.221:82/zentao/user-login.json?zentaosid=6gci293aa6eav8eap3i2l5bi51&password=cnte@123&account=ops)
              result=$(curl -s -b /var/opt/gitlab/gitaly/user.info http://192.168.1.221:82/zentao/story-view-$numStr.json)
              if [[ ${result:0:1} == "{" ]]; then
                echo "[DEBUG]需求号存在："$numStr
                storys[$numStr]=1
              else
                echo "[ERROR]提交信息检查不通过!!!"
                echo "[ERROR]请确认需求编号[$numStr]是否存在"
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
              echo "[DEBUG]开始校验BUG号："$numStr
              result=$(curl -s -H "User-Agent=Mozilla/5.0" -c /var/opt/gitlab/gitaly/user.info -b /var/opt/gitlab/gitaly/user.info http://192.168.1.221:82/zentao/user-login.json?zentaosid=6gci293aa6eav8eap3i2l5bi51&password=cnte@123&account=ops)
              result=$(curl -s -b /var/opt/gitlab/gitaly/user.info http://192.168.1.221:82/zentao/bug-view-$numStr.json)
              if [[ ${result:0:1} == "{" ]]; then
                echo "[DEBUG]BUG号存在："$numStr
                bugs[$numStr]=1
              else
                echo "[ERROR]提交信息检查不通过!!!"
                echo "[ERROR]请确认BUG编号[$numStr]是否存在"
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
    echo "本次提交关联禅道需求号："${!storys[*]}
    echo "本次提交关联禅道BUG号："${!bugs[*]}

  done
else
  echo "该项目无需校验"
fi
