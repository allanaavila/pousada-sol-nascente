let next = document.querySelector('.next');
let prev = document.querySelector('.prev');

next.addEventListener('click', function(){
    let items = document.querySelectorAll('.item');
    document.querySelector('.slide').appendChild(items[0]);
});

prev.addEventListener('click', function(){
    let items = document.querySelectorAll('.item');
    document.querySelector('.slide').prepend(items[items.length -1]);
});

function reservarQuarto(paginaQuarto) {
    var checkinDate = document.getElementById('checkin').value;
    var checkoutDate = document.getElementById('checkout').value;

    sessionStorage.setItem('reservaCheckin', checkinDate);
    sessionStorage.setItem('reservaCheckout', checkoutDate);

    window.location.href = paginaQuarto;
}

