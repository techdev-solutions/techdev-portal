function getURI(url) {
    // abuse the <a> element to easily parse a URL like http://localhost:8081/oauth/authorize?client_id=trackr
    var a = document.createElement('a');
    a.href = url;
    return a.pathname + a.search;
}

function loginCallback(newUrl) {
    var redirectUrl = newUrl;

    // If we get a complete URL from the backend we convert it to a URI.
    // This URI should *not* contain any proxy paths like /portal/.
    if(redirectUrl.indexOf('http://') === 0 || redirectUrl.indexOf('https://')) {
        redirectUrl = getURI(redirectUrl);
    }

    // if the redirectUrl is absolute, make it relative so the proxy path like /portal/ is used.
    window.location.href = redirectUrl.indexOf('/') === 0 ? redirectUrl.substr(1) : redirectUrl;
}

function loginError(response, data, error) {
    // todo: better error handling
    alert(error);
}

function googleCallback(authResult) {
    if(authResult.access_token) {
        $.get('login', {'access-token': authResult.access_token})
            .done(loginCallback)
            .fail(loginError);
    }
}