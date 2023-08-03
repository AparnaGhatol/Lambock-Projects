document.querySelectorAll('.form-submit-confirm').forEach(($item) => {
    $item.addEventListener('submit', (event) => {
        if (!confirm(event.currentTarget.getAttribute('data-msg-confirm'))) {
            event.preventDefault();
            return false;
        }
        return true;
    });
});

 function changePageSize() {
    document.getElementById("changeSize_form").submit();
 }
