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
    if(redirectUrl.indexOf('http://') === 0 || redirectUrl.indexOf('https://') === 0) {
        redirectUrl = getURI(redirectUrl);
    }

    // if the redirectUrl is absolute, make it relative so the proxy path like /portal/ is used.
    window.location.href = redirectUrl.indexOf('/') === 0 ? redirectUrl.substr(1) : redirectUrl;
}

function loginError(response, data, error) {
    if(response.status === 500) {
        viewModel.errorMessage('There was an internal server error logging in. Please contact an administrator.');
    }
    if(response.status === 401) {
        var responseEntity = JSON.parse(response.responseText);
        viewModel.errorMessage(responseEntity.message);
    }
    viewModel.showError(true);
}

var viewModel = {
    showError: ko.observable(false),
    errorMessage: ko.observable()
};

function onSignIn(googleUser) {
    // Reset error message after sign in with Google happened, errors can only happen from our login endpoint!
    viewModel.showError(false);
    viewModel.errorMessage('');

    var idToken = googleUser.getAuthResponse().id_token;
    $.get('login', { 'access-token': idToken })
      .done(loginCallback)
      .fail(loginError);
}

