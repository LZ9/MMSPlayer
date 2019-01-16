# MMSPlayer播放器
基于 [IJK播放器](https://github.com/Bilibili/ijkplayer) 封装的播放器依赖库，核心播放控件使用IJKPlayer，我通过接口封装来解耦，方便调用和替换。
大家可以在基础播放器上自定义菜单页面等等，详细demo可以git工程查看或者直接进入 [VideoActivity.kt](https://github.com/LZ9/MMSPlayer/blob/master/app/src/main/java/com/lodz/android/mmsplayerdemo/widget/VideoActivity.kt) 查看。

## 目录
- [1、添加Gradle依赖](https://github.com/LZ9/MMSPlayer#1添加gradle依赖)
- [2、使用方法](https://github.com/LZ9/MMSPlayer#2使用方法)
- [3、测试Demo（Kotlin编写）](https://github.com/LZ9/MMSPlayer#3测试demokotlin编写)
- [扩展](https://github.com/LZ9/MMSPlayer#扩展)

## 1、添加Gradle依赖
```
    implementation 'cn.lodz:mmsplayer:1.0.6'
```

## 2、使用方法
我封装了MmsVideoView控件，大家可以通过IVideoPlayer接口来获取控件对象，接口里有播放器的控制方法，可以通过调用接口方法来操作播放器逻辑。

#### 1）在xml里添加播放器控件
```
    <com.lodz.android.mmsplayer.impl.MmsVideoView
        android:id="@+id/video_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </com.lodz.android.mmsplayer.impl.MmsVideoView>
```

#### 2）在代码中获取控件对象
```
    private val mVideoPlayer : IVideoPlayer by lazy {
        findViewById<MmsVideoView>(R.id.video_view)
    }
```

#### 3）操作播放器方法
```
    // 初始化（使用默认的播放器配置）
    mVideoPlayer.init()

    // 初始化（自定义播放器配置）
    val setting = IjkPlayerSetting.getDefault()
    setting.aspectRatioType = IRenderView.AR_ASPECT_FILL_PARENT
    mVideoPlayer.init(setting)

    // 设置监听器回调
    mVideoPlayer.setListener(object : MmsVideoView.Listener{
        override fun onPrepared() {
            // 播放器就绪（播放路径解析完成）
        }

        override fun onBufferingStart() {
            // 开始缓冲
        }

        override fun onBufferingEnd() {
            // 结束缓冲
        }

        override fun onCompletion() {
            // 播放完成
        }

        override fun onError(errorType: Int, msg: String?) {
            // 播放出现异常
        }

    })

    // 设置播放路径
    mVideoPlayer.setVideoPath(url)

    // 开始播放
    mVideoPlayer.start()

    // 是否正在播放
    mVideoPlayer.isPlaying()

    // 是否暂停
    mVideoPlayer.isPause()

    // 是否播放结束
    mVideoPlayer.isCompleted()

    // 是否已经设置播放地址
    mVideoPlayer.isAlreadySetPath()

    // 暂停播放
    mVideoPlayer.pause()

    // 重新播放
    mVideoPlayer.resume()

    // 停止播放释放资源
    mVideoPlayer.release()

    // 获取缓存进度百分比
    mVideoPlayer.getBufferPercentage()

    // 获取当前播放进度
    mVideoPlayer.getCurrentPlayPosition()

    // 获取播放失败时的进度
    mVideoPlayer.getBreakPosition()

    // 获取视频总时长
    mVideoPlayer.getVideoDuration()

    // 设置播放位置
    mVideoPlayer.seekTo(1000)

    // 设置播放位置并开始播放
    mVideoPlayer.seekAndStart(1000)

    // 设置播放控制器（控制器需要实现IMediaController接口）
    mVideoPlayer.setMediaController(controller)

    // 设置宽高比
    //（IRenderView.AR_ASPECT_FIT_PARENT、IRenderView.AR_ASPECT_FILL_PARENT、
    // IRenderView.AR_ASPECT_WRAP_CONTENT、IRenderView.AR_MATCH_PARENT、
    // IRenderView.AR_16_9_FIT_PARENT、IRenderView.AR_4_3_FIT_PARENT）
    mVideoPlayer.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT)

    // 获取视频信息
    mVideoPlayer.getMediaInfo()

    // 切换宽高比
    mVideoPlayer.toggleAspectRatio()

    // 获取当前渲染view名称
    mVideoPlayer.getRenderText(context)

    // 获取当前播放器名称
    mVideoPlayer.getPlayerText(context)
```

- init()和init(IjkPlayerSetting setting)根据需要选择一个调用即可
- setVideoPath(String path)可以传入url和file，暂不支持资源id（例如R.raw.xxx）
- IjkPlayerSetting里面可以对一些播放器参数进行配置，例如播放器类型（支持原生、IJK、EXO），宽高比、硬解或软解等等

## 3、测试Demo（Kotlin编写）
- [SimpleVideoActivity.kt](https://github.com/LZ9/MMSPlayer/blob/master/app/src/main/java/com/lodz/android/mmsplayerdemo/simple/SimpleVideoActivity.kt) 接入最基础的播放器控件，方便大家了解基本调用方式
- [VideoActivity.kt](https://github.com/LZ9/MMSPlayer/blob/master/app/src/main/java/com/lodz/android/mmsplayerdemo/widget/VideoActivity.kt) 我自定义了一些播放器菜单控件，并集成在 [MediaView.kt](https://github.com/LZ9/MMSPlayer/blob/master/app/src/main/java/com/lodz/android/mmsplayerdemo/video/view/MediaView.kt) 里面，供大家参考
- 大家可以git工程下来，运行查看。

## 扩展
- [更新记录](https://github.com/LZ9/MMSPlayer/blob/master/mmsplayer/readme_mmsplayer_update.md)
- [回到顶部](https://github.com/LZ9/MMSPlayer#mmsplayer播放器)

## License
- [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Copyright 2018 Lodz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

s under the License.