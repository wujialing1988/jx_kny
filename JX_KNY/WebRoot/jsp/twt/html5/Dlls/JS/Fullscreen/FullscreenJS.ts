class Fullscreen
{
    FullscreenBool: boolean;
    constructor()
    {
        this.FullscreenBool = true;
        var viewFullScreen = document.getElementById("FullscreenButton");
        if (viewFullScreen)
        {
            /*(<any>window).Touch ? viewFullScreen.ontouchstart = this.Full : */viewFullScreen.onclick = this.Full;
        }
    }
    Full()
    {
        if (document.webkitFullscreenElement || document.fullscreenElement || (<any>document).mozFullScreenElement || (<any>document).msFullscreenElement)
        {
            if (document.exitFullscreen)
            {
                document.exitFullscreen();
            }
            else if ((<any>document).msExitFullscreen)
            {
                (<any>document).msExitFullscreen();
            }
            else if ((<any>document).mozCancelFullScreen)
            {
                (<any>document).mozCancelFullScreen();
            }
            else if (document.webkitCancelFullScreen)
            {
                document.webkitCancelFullScreen();
            }
        }
        else
        {
            var docElm = document.documentElement;
            if (docElm.requestFullscreen)
            {
                docElm.requestFullscreen();
            }
            else if ((<any>docElm).msRequestFullscreen)
            {
                (<any>docElm).msRequestFullscreen();
            }
            else if ((<any>docElm).mozRequestFullScreen)
            {
                (<any>docElm).mozRequestFullScreen();
            }
            else if (docElm.webkitRequestFullScreen)
            {
                docElm.webkitRequestFullScreen();
            }
        }
    }
}
window.addEventListener("load", function ()
{
    var Full = new Fullscreen();
});