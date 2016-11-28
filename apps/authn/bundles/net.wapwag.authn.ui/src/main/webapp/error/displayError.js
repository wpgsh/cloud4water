/**
 * Used for display error message from url query string
 * Created by Administrator on 2016/11/28 0028.
 */
function getQueryString(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]).replace(/\+/g, " ");
}

var ERROR = "error";
var ERROR_DESC = "error_description";

$(function () {
    var error, errorDesc;
    var queryString = location.search;

    if (queryString) {
        error = getQueryString(ERROR);
        errorDesc = getQueryString(ERROR_DESC);

        if ($.trim(error) !== "") {
            $("#error").text(error);
        }

        if ($.trim(errorDesc) !== "") {
            $("#errorDesc").text(errorDesc);
        }
    }
});