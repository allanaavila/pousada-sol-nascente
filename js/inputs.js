document.getElementById('toggleDates').addEventListener('click', function () {
    var dateInputs = document.getElementById('dateInputs');
    dateInputs.style.display = dateInputs.style.display === 'none' ? 'flex' : 'none';

    // Inicialize Flatpickr nos inputs de texto
    flatpickr('#checkin');
    flatpickr('#checkout');
});
