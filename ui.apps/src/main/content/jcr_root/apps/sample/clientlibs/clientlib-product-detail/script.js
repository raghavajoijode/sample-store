function changeProductImage(event, src) {
    /* Change the main image when a thumbnail is clicked */
    document.getElementById('mainImage').src = src;
    document.querySelectorAll('.thumbnail').forEach(thumb => thumb.classList.remove('active'));
    event.target.classList.add('active');
}