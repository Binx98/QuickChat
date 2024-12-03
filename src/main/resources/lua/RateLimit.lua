--[[脚本传入参数：限流key、限流次数、限流时间范围]]
local key = KEYS[1]
local count = tonumber(ARGV[1])
local time = tonumber(ARGV[2])

--[[调用get方法，限流Key对应次数]]
local current = redis.call('get', key)

--[[限流Key对应次数 < 限制次数，放行！]]
--[[限流Key对应次数 > 限制次数，结束！]]
if current and tonumber(current) > count then
    return tonumber(current)
end

--[[限流Key对应次数 + 1]]
current = redis.call('incr', key)

--[[限流Key对应次数 = 1，设置限流时间为过期时间]]
if tonumber(current) == 1 then
    redis.call('expire', key, time)
end
return tonumber(current)