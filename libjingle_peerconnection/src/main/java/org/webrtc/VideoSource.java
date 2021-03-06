/*
 *  Copyright 2013 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package org.webrtc;

import javax.annotation.Nullable;

/**
 * Java wrapper of native AndroidVideoTrackSource.
 */
@JNINamespace("webrtc::jni")
public class VideoSource extends MediaSource {
  private final NativeCapturerObserver capturerObserver;

  public VideoSource(long nativeSource) {
    super(nativeSource);
    this.capturerObserver = new NativeCapturerObserver(nativeGetInternalSource(nativeSource));
  }

  // TODO(bugs.webrtc.org/9181): Remove.
  VideoSource(long nativeSource, SurfaceTextureHelper surfaceTextureHelper) {
    super(nativeSource);
    this.capturerObserver =
        new NativeCapturerObserver(nativeGetInternalSource(nativeSource), surfaceTextureHelper);
  }

  /**
   * Calling this function will cause frames to be scaled down to the requested resolution. Also,
   * frames will be cropped to match the requested aspect ratio, and frames will be dropped to match
   * the requested fps. The requested aspect ratio is orientation agnostic and will be adjusted to
   * maintain the input orientation, so it doesn't matter if e.g. 1280x720 or 720x1280 is requested.
   */
  public void adaptOutputFormat(int width, int height, int fps) {
    nativeAdaptOutputFormat(nativeSource, width, height, fps);
  }

  public VideoCapturer.CapturerObserver getCapturerObserver() {
    return capturerObserver;
  }

  @Override
  public void dispose() {
    capturerObserver.dispose();
    super.dispose();
  }

  // Returns source->internal() from webrtc::VideoTrackSourceProxy.
  private static native long nativeGetInternalSource(long source);
  private static native void nativeAdaptOutputFormat(long source, int width, int height, int fps);
}
