document.getElementById('toggleDates').addEventListener('click', function () {
    document.getElementById('dateModal').style.display = 'block';
});

document.getElementById('closeModal').addEventListener('click', function () {
    document.getElementById('dateModal').style.display = 'none';
});

document.getElementById('applyDates').addEventListener('click', function () {
    var checkinDate = document.getElementById('checkinModal').value;
    var checkoutDate = document.getElementById('checkoutModal').value;

    // Verificar se as datas são válidas
    if (isValidDates(checkinDate, checkoutDate)) {
        // Atualize os campos de Check-in e Check-out principais
        document.getElementById('checkin').value = checkinDate;
        document.getElementById('checkout').value = checkoutDate;

        // Atualize o texto do link com as datas selecionadas
        var dateLinkText = 'Check-in: ' + formatDate(checkinDate) + ' - Check-out: ' + formatDate(checkoutDate);
        document.getElementById('toggleDates').innerHTML = dateLinkText;

        // Feche o modal
        document.getElementById('dateModal').style.display = 'none';
    } else {
        alert('Por favor, selecione datas válidas.');
    }
});

// Adicione esta função para formatar a data (opcional)
function formatDate(dateString) {
    var options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString.replace(/-/g, '/')).toLocaleDateString('en-US', options);
}

// Adicione esta função para validar as datas
function isValidDates(checkinDate, checkoutDate) {
    var today = new Date();
    var checkin = new Date(checkinDate.replace(/-/g, '/'));
    var checkout = new Date(checkoutDate.replace(/-/g, '/'));

    // Verifica se as datas não são anteriores ao dia atual
    if (checkin < today || checkout < today) {
        return false;
    }

    // Verifica se o Checkout não é feito no mesmo dia do Check-in
    if (checkout <= checkin) {
        return false;
    }

    return true;
}

// Adicione esta função para fechar o modal se clicar fora dele
window.onclick = function (event) {
    var modal = document.getElementById('dateModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
};

