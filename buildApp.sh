#！/bin/sh

path=`flutter build apk`

apppath=${path#*Built }
pathOne=${apppath%'.apk'*}.apk

echo $pathOne

#!time3=$(date "+%Y-%m-%d_%H_%M_%S")

cp $pathOne  /Library/WebServer/Documents/app/yingfa.apk

apk=yuehaiguoji.apk







