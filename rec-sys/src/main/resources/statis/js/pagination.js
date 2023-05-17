function paginate(ads, currentPage, pageSize) {
    var startIndex = currentPage * pageSize;
    var endIndex = startIndex + pageSize;
    var slicedAds = ads.slice(startIndex, endIndex);
    renderAds(slicedAds);
    renderPagination(ads.length, currentPage, pageSize);
}

function renderAds(ads) {
    var tbody = $('.ads-list tbody');
    tbody.empty();
    ads.forEach(function(ad) {
        var tr = $('<tr>');
        var titleTd = $('<td>').text(ad.title);
        var descriptionTd = $('<td>').text(ad.description);
        var priceTd = $('<td>').text(ad.price);
        tr.append(titleTd);
        tr.append(descriptionTd);
        tr.append(priceTd);
        tbody.append(tr);
    });
}

function renderPagination(totalItems, currentPage, pageSize) {
    var totalPages = Math.ceil(totalItems / pageSize);
    var pagination = $('.pagination');
    pagination.empty();
    for (var i = 0; i < totalPages; i++) {
        var li = $('<li>');
        var link = $('<a>').attr('href', '#').text(i + 1);
        li.append(link);
        pagination.append(li);
    }
    pagination.find('li').eq(currentPage).addClass('active');
    pagination.find('a').click(function (event) {
        event.preventDefault();
        var pageNumber = $(this).text() - 1;
        paginate(ads, pageNumber, pageSize);

    })
    $(document).ready(function () {
        $('.pagination').on('click', 'a', function (event) {
            event.preventDefault();
            var pageNumber = $(this).text() - 1;
            paginate(ads, pageNumber, pageSize);
        });
    });
}