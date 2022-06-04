package com.app.cryptok.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class streamBean {
    @SerializedName("appKey")
    @Expose
    private String appKey;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;
    @SerializedName("mediaSessionId")
    @Expose
    private String mediaSessionId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("published")
    @Expose
    private Boolean published;
    @SerializedName("hasVideo")
    @Expose
    private Boolean hasVideo;
    @SerializedName("hasAudio")
    @Expose
    private Boolean hasAudio;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sdp")
    @Expose
    private String sdp;
    @SerializedName("audioCodec")
    @Expose
    private String audioCodec;
    @SerializedName("videoCodec")
    @Expose
    private String videoCodec;
    @SerializedName("record")
    @Expose
    private Boolean record;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("minBitrate")
    @Expose
    private Integer minBitrate;
    @SerializedName("maxBitrate")
    @Expose
    private Integer maxBitrate;
    @SerializedName("quality")
    @Expose
    private Integer quality;
    @SerializedName("history")
    @Expose
    private Boolean history;
    @SerializedName("gop")
    @Expose
    private Integer gop;
    @SerializedName("fps")
    @Expose
    private Integer fps;
    @SerializedName("audioBitrate")
    @Expose
    private Integer audioBitrate;
    @SerializedName("codecImpl")
    @Expose
    private String codecImpl;
    @SerializedName("transport")
    @Expose
    private String transport;
    @SerializedName("cvoExtension")
    @Expose
    private Boolean cvoExtension;
    @SerializedName("createDate")
    @Expose
    private Long createDate;
    @SerializedName("mediaType")
    @Expose
    private String mediaType;
    @SerializedName("mediaProvider")
    @Expose
    private String mediaProvider;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("parentMediaSessionId")
    @Expose
    private String parentMediaSessionId;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMediaSessionId() {
        return mediaSessionId;
    }

    public void setMediaSessionId(String mediaSessionId) {
        this.mediaSessionId = mediaSessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public Boolean getRecord() {
        return record;
    }

    public void setRecord(Boolean record) {
        this.record = record;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getMinBitrate() {
        return minBitrate;
    }

    public void setMinBitrate(Integer minBitrate) {
        this.minBitrate = minBitrate;
    }

    public Integer getMaxBitrate() {
        return maxBitrate;
    }

    public void setMaxBitrate(Integer maxBitrate) {
        this.maxBitrate = maxBitrate;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Boolean getHistory() {
        return history;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    public Integer getGop() {
        return gop;
    }

    public void setGop(Integer gop) {
        this.gop = gop;
    }

    public Integer getFps() {
        return fps;
    }

    public void setFps(Integer fps) {
        this.fps = fps;
    }

    public Integer getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(Integer audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public String getCodecImpl() {
        return codecImpl;
    }

    public void setCodecImpl(String codecImpl) {
        this.codecImpl = codecImpl;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Boolean getCvoExtension() {
        return cvoExtension;
    }

    public void setCvoExtension(Boolean cvoExtension) {
        this.cvoExtension = cvoExtension;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaProvider() {
        return mediaProvider;
    }

    public void setMediaProvider(String mediaProvider) {
        this.mediaProvider = mediaProvider;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getParentMediaSessionId() {
        return parentMediaSessionId;
    }

    public void setParentMediaSessionId(String parentMediaSessionId) {
        this.parentMediaSessionId = parentMediaSessionId;
    }
}
