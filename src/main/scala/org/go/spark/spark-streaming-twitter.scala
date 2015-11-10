package org.go.spark

import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Durations, StreamingContext}

object TwitterStreaming {
  def main(args: Array[String]): Unit = {
    //sparkconf 設定
    val conf = new SparkConf().setAppName("Twitter Streaming")
    // sparkstreamcontext設定
    val ssc = new StreamingContext(conf, Durations.minutes(1L))
    val filter = if (args.isEmpty) Nil else args.toList
    val stream = TwitterUtils.createStream(ssc, None, filter)
    
    stream
      //flatMap->stream内の要素を調べてstreamに返す
      .flatMap { status =>
      val text = status.getText

      val analyzer = new JapaneseAnalyzer
      val tokenStream = analyzer.tokenStream("", text)
      val charAttr = tokenStream.addAttribute(classOf[CharTermAttribute])
      
      //resetメソッドを呼んだ後に、incrementTokenメソッドでTokenを読み進めていく
      tokenStream.reset()
      
      try {
        Iterator
          .continually(tokenStream.incrementToken())
          .takeWhile(identity)
          .map(_ => charAttr.toString)
          .toVector
      } finally {
        tokenStream.end()
        tokenStream.close()
      }
    }
      .map(word => (word, 1))
      .reduceByKey((a, b) => a + b)
      .saveAsTextFiles("log/twitter")

    ssc.start()
    ssc.awaitTermination()
  }
}
