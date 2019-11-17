#!/bin/bash
#budowanie
FAIL=0
./build.sh seraver &
./build.sh client &
for job in `jobs -p`
do
echo $job
    wait $job || let "FAIL+=1"
done

echo $FAIL

if [ "$FAIL" == "0" ];
then
  echo "YAY!"
else
  echo "Nie udało sie skompilować! "
  exit
fi

( cd docker ; docker-compose up --build)