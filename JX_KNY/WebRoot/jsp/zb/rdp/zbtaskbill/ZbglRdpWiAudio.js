/**
 * 播放附件模块中的音频文件
 */
Ext.ns('ZbglRdpWiAudio');
ZbglRdpWiAudio.getAudio = function(){
    if(null == ZbglRdpWiAudio.audio)  ZbglRdpWiAudio.audio = new Audio();
    return ZbglRdpWiAudio.audio;
}
ZbglRdpWiAudio.play = function(idx){
    var audio = ZbglRdpWiAudio.getAudio();
    audio.pause();
    audio.src = ctx + '/attachment!download.action?id=' + idx;
    audio.play();    
}
