# SparkSTreamingTwitter
Twieetをリアルタイムで取得、形態素解析集計

## jarファイル作成

```
sbt assembly
```

## 実行

```
spark-submit --class org.go.spark.TwitterStreaming target/scala-2.11/spark-streaming-twitter-assembly-0.0.1.jar 
```

## 終了

```
control+cで強制終了
```

##logの保存場所

$WORK/log/twitter-[unixtime]

## 形態素解析結果+count

```
(crying,1)
(ラブカストーン,1)
(天才,1)
(bxpchupdhr,1)
(ー,3)
(結局,1)
(perut,1)
(疑惑,1)
(smsa,1)
(me,6)
(conheceu,1)
(are,2)
```

