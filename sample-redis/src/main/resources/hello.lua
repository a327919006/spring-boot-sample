---
--- https://www.lua.org/
--- Created by ChenNan.
--- DateTime: 2024/8/20 22:50
---
print("hello lua!")

-- 声明字符串，可以用单引号或双引号，
local str = 'hello'
-- 字符串拼接可以使用 ..
local str2 = 'hello' .. 'world'
-- 声明数字
local num = 21
-- 声明布尔类型
local flag = true
-- 声明数组 ，key为角标的 table
local arr = { 'java', 'python', 'lua' }
-- 声明table，类似java的map
local map = { name = 'Jack', age = 21 }

-- 访问数组，lua数组的角标从1开始
print(arr[1])
-- 访问table
print(map['name'])
print(map.name)

-- 遍历数组
for index, value in ipairs(arr) do
    print(index, value)
end

-- 遍历table
for key, value in pairs(map) do
    print(key, value)
end

-- 定义一个函数，用来打印数组
function printArr(currArray)
    if not currArray then
        print('数组不能为空！')
    end
    for index, value in ipairs(currArray) do
        print(index, value)
    end
end

printArr(arr)