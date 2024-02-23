document.addEventListener('DOMContentLoaded', function () {

    const diaria = document.getElementById('valorDiaria');
    const taxaImposto = document.querySelector('.taxa');
    const valorTotal = document.querySelector('.valorTotal');

    document.getElementById('applyDates').addEventListener('click', function () {
        var checkinDate = document.getElementById('checkinModal').value;
        var checkoutDate = document.getElementById('checkoutModal').value;

        if (isValidDates(checkinDate, checkoutDate)) {
            
            document.getElementById('checkin').value = checkinDate;
            document.getElementById('checkout').value = checkoutDate;
            var dateLinkText = '<i class="fa fa-calendar-days"></i> ' + formatDate(checkinDate) + ' - ' + formatDate(checkoutDate);
            document.getElementById('toggleDates').innerHTML = dateLinkText;

            // Calcular o número de diárias entre o check-in e o check-out
            var numberOfDays = calculateNumberOfDays(checkinDate, checkoutDate);

            var dailyRate = parseFloat(diaria.innerText.replace('R$', '').replace(',', '.'));

            var totalBeforeTax = dailyRate * numberOfDays;
            var taxRate = 0.10;

            var taxAmount = totalBeforeTax * taxRate;

            var finalAmount = totalBeforeTax + taxAmount;

            // Atualizar os elementos HTML com os resultados
            diaria.innerText = 'R$ ' + dailyRate.toFixed(2);
            taxaImposto.innerText = 'R$ ' + taxAmount.toFixed(2);
            valorTotal.innerText = 'R$ ' + finalAmount.toFixed(2);

            console.log('diaria:', diaria); // Verifique se diaria é encontrado corretamente
var dailyRate = parseFloat(diaria.innerText.replace('R$', '').replace(',', '.'));
console.log('dailyRate:', dailyRate);

            document.getElementById('dateModal').style.display = 'none';
        } else {
            alert('Por favor, selecione datas válidas.');
        }
    });

    // Função para calcular o número de diárias entre o check-in e o check-out
    function calculateNumberOfDays(checkinDate, checkoutDate) {
        var oneDay = 24 * 60 * 60 * 1000; // horas * minutos * segundos * milissegundos
        var checkin = new Date(checkinDate);
        var checkout = new Date(checkoutDate);

        var diffDays = Math.round(Math.abs((checkin - checkout) / oneDay));

        return diffDays;
    }

    // Função para validar as datas
    function isValidDates(checkinDate, checkoutDate) {
        // Implemente a lógica de validação das datas aqui
        return true; // Por enquanto, retorna true sempre
    }

    // Função para formatar a data (opcional)
    function formatDate(dateString) {
        var date = new Date(dateString);
        var day = date.getDate();
        var month = date.getMonth() + 1;
        var year = date.getFullYear();

        return padNumber(day) + '/' + padNumber(month) + '/' + year;
    }

    // Função auxiliar para garantir que os números tenham dois dígitos
    function padNumber(number) {
        return (number < 10 ? '0' : '') + number;
    }

});