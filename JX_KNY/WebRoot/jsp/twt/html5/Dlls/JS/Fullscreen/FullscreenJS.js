var Fullscreen = (function () {
    function Fullscreen() {
        this.FullscreenBool = true;
        var viewFullScreen = document.getElementById("FullscreenButton");
        if (viewFullScreen) {
            /*(<any>window).Touch ? viewFullScreen.ontouchstart = this.Full : */ viewFullScreen.onclick = this.Full;
        }
    }
    Fullscreen.prototype.Full = function () {
        if (document.webkitFullscreenElement || document.fullscreenElement || document.mozFullScreenElement || document.msFullscreenElement) {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            }
            else if (document.msExitFullscreen) {
                document.msExitFullscreen();
            }
            else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            }
            else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
        }
        else {
            var docElm = document.documentElement;
            if (docElm.requestFullscreen) {
                docElm.requestFullscreen();
            }
            else if (docElm.msRequestFullscreen) {
                docElm.msRequestFullscreen();
            }
            else if (docElm.mozRequestFullScreen) {
                docElm.mozRequestFullScreen();
            }
            else if (docElm.webkitRequestFullScreen) {
                docElm.webkitRequestFullScreen();
            }
        }
    };
    return Fullscreen;
})();
window.addEventListener("load", function () {
    var Full = new Fullscreen();
});
//# sourceMappingURL=FullscreenJS.js.map