document.addEventListener('DOMContentLoaded', function () {

    const diaria = document.getElementById('valorDiaria');
    const taxaImposto = document.getElementById('taxa');
    const valorTotal = document.getElementById('valorTotal');
    const valorParceladoFinal = document.getElementById('valorParceladoFinal');
    const opcaoParcelar = document.getElementById('parcelar');
    const resumoReservaContainer = document.querySelector('.reservation-summary');
    const applyDatesButton = document.getElementById('applyDates');



    if (applyDatesButton) {
        applyDatesButton.addEventListener('click', function () {

        var checkinDate = document.getElementById('checkinModal').value;
        var checkoutDate = document.getElementById('checkoutModal').value;

        if (isValidDates(checkinDate, checkoutDate)) {
            document.getElementById('checkin').value = checkinDate;
            document.getElementById('checkout').value = checkoutDate;
            var dateLinkText = '<i class="fa fa-calendar-days"></i> ' + formatDate(checkinDate) + ' - ' + formatDate(checkoutDate);
            document.getElementById('toggleDates').innerHTML = dateLinkText;

            var numberOfDays = calculateNumberOfDays(checkinDate, checkoutDate);
            var dailyRate = parseFloat(diaria.innerText.replace('R$', '').replace(',', '.'));
            var totalBeforeTax = dailyRate * numberOfDays;
            var taxRate = 0.10;
            var taxAmount = totalBeforeTax * taxRate;
            var finalAmount = totalBeforeTax + taxAmount;

            diaria.innerText = 'R$ ' + dailyRate.toFixed(2);
            taxaImposto.innerText = 'R$ ' + taxAmount.toFixed(2);
            valorTotal.innerText = 'R$ ' + finalAmount.toFixed(2);

            updateReservationSummary(checkinDate, checkoutDate, finalAmount);

            document.getElementById('dateModal').style.display = 'none';
        } else {
            alert('Por favor, selecione datas válidas.');
        }
    });
}
    
    function calculateNumberOfDays(checkinDate, checkoutDate) {
        var oneDay = 24 * 60 * 60 * 1000;
        var checkin = new Date(checkinDate);
        var checkout = new Date(checkoutDate);
        var diffDays = Math.round(Math.abs((checkin - checkout) / oneDay));
        return diffDays;
    }

    function isValidDates(checkinDate, checkoutDate) {
        var checkin = new Date(checkinDate);
        var checkout = new Date(checkoutDate);
        return checkin < checkout;
    }

    function formatDate(dateString) {
        var date = new Date(dateString);
        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();
        return padNumber(day) + '/' + padNumber(month) + '/' + year;
    }

    function padNumber(number) {
        return (number < 10 ? '0' : '') + number;
    }

    //PAGAMENTO 
    function pagar() {
        var valorFinal = parseFloat(valorTotal.innerText.replace('R$', '').replace(',', '.'));
        var opcaoSelecionada = opcaoParcelar.value;

        if (opcaoSelecionada === '1') {  
            var descontoAVista = 0.15; 
            var valorComDesconto = valorFinal - (valorFinal * descontoAVista);
            valorParceladoFinal.innerText = 'R$ ' + valorComDesconto.toFixed(2);
        } else if (opcaoSelecionada === '2') { 
            var valorEntrada = valorFinal * 0.25;  
            valorParceladoFinal.innerText = 'R$ ' + valorEntrada.toFixed(2);
        }
    }

    // Associando a função pagar ao botão
    opcaoParcelar.addEventListener('change', function () {
        pagar();
    });
    pagar();

    document.getElementById('actualPaymentForm').addEventListener('submit', function (event) {
        event.preventDefault();
        pagar();
    });

    function updateReservationSummary(checkinDate, checkoutDate, finalAmount) {
        document.getElementById('checkin').textContent = 'Data de Check-in: ' + formatDate(checkinDate);
        document.getElementById('checkout').textContent = 'Data de Check-out: ' + formatDate(checkoutDate);
        document.getElementById('valorParceladoFinal').textContent = 'Total: R$ ' + finalAmount.toFixed(2);
    
        console.log('Atualizando resumo da reserva:', checkinDate, checkoutDate, finalAmount);
    }
});


