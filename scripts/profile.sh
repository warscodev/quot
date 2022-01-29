#!/usr/bin/env bash

# 쉬고 있는 profile 찾기 : real1이 사용중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음
function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/_profile)

    echo "RESPONSE_CODE : $RESPONSE_CODE"

    if [ "${RESPONSE_CODE}" -ge 400 ]

    then
      echo " then - CURRENT_PROFILE : real2"
      CURRENT_PROFILE=real2
    else
      echo "else - CURRENT_PROFILE : (curl -s http://localhost/_profile)"
      CURRENT_PROFILE=$(curl -s http://localhost/_profile)
      echo "CURRENT_PROFILE : ${CURRENT_PROFILE}"
    fi

    if [ "${CURRENT_PROFILE}" == real1 ]
    then
      echo "IDLE_PROFILE : real2"
      IDLE_PROFILE=real2
    else
      echo "IDLE_PROFILE : real1"
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 PROFILE의 PORT 찾기
function find_idle_port()
{
    echo " > find_idle_port 실행"
    IDLE_PROFILE=$(find_idle_profile)
    echo " > IDLE_PROFILE on find_idle_port : $IDLE_PROFILE"
    if [ "${IDLE_PROFILE}" == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}