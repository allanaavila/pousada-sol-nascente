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
    // Converte a string da data para um objeto Date
    var date = new Date(dateString.replace(/-/g, '/'));

    // Obtém os componentes da data (dia, mês e ano)
    var day = date.getDate();
    var month = date.getMonth() + 1; // Adiciona 1 porque os meses começam do zero
    var year = date.getFullYear();

    // Formata a data no formato desejado
    var formattedDate = padNumber(day) + '/' + padNumber(month) + '/' + year;

    return formattedDate;
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

// Função auxiliar para garantir que os números tenham dois dígitos
function padNumber(number) {
    return (number < 10 ? '0' : '') + number;
}

