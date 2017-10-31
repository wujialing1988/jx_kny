/**
 * 播放附件模块中的音频文件
 */
Ext.ns('AttAudio');
AttAudio.getAudio = function(){
    if(null == AttAudio.audio)  AttAudio.audio = new Audio();
    return AttAudio.audio;
}
AttAudio.play = function(idx){
    var audio = AttAudio.getAudio();
    audio.pause();
    audio.src = ctx + '/attachment!download.action?id=' + idx;
    audio.play();    
}
