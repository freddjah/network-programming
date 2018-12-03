window.onload = function() {

    var defaultCurrencyId = document.getElementById('currency_id').value;
    if (defaultCurrencyId && defaultCurrencyId > 0) {
        document.querySelector('table button[data-currency-id="' + defaultCurrencyId + '"]').classList.add('pure-button-primary');
    }

    var buttons = document.querySelectorAll('table button');
    buttons.forEach(function (button) {
        button.addEventListener('click', function(event) {

            event.preventDefault();
            var currencyId = button.getAttribute('data-currency-id');

            document.getElementById('currency_id').value = currencyId;

            buttons.forEach(function (button) {
                button.classList.remove('pure-button-primary');
            });
            button.classList.add('pure-button-primary');
        })
    });
}


