

function toggleFavorite(heartIconId) {
  var heartIcon = document.getElementById(heartIconId);
  if (heartIcon) {
      heartIcon.classList.toggle('favorite');
  }
}