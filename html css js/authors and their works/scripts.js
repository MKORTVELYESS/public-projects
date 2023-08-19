//Form submission needs to be prevented when submit button is clicked
document.addEventListener("DOMContentLoaded", function () {
  const searchForm = document.getElementsByTagName("form")[0];
  searchForm.addEventListener("submit", function (event) {
    event.preventDefault();
  });
});

//Add Evenet listener to submit button
const inputSubmit = document.querySelector(
  '.author-search input[type="submit"]'
);
inputSubmit.addEventListener("click", async function () {
  const inputAuthor = document.getElementById("input-author-name");
  const authorName = inputAuthor.value;
  const authorsArr = await getAuthorsList(authorName);
  authorsArr.forEach((author) => {
    renderAuthorCard(author);
  });
});

//Fetch API for author list matching function input
async function getAuthorsList(authorName) {
  const url = `https://openlibrary.org/search/authors.json?q=${authorName}`;
  let response = await fetch(url);
  response = await response.json();
  return response.docs;
}

//Render the author cards into the HTML with the required information and img
async function renderAuthorCard(author) {
  const resultsContainer = document.querySelector(
    ".container.search-results.row"
  );
  resultsContainer.innerHTML = ""; //Remove the contents from previous query
  const imgURL = `https://covers.openlibrary.org/a/olid/${author.key}-M.jpg`;
  const response = await fetch(imgURL);
  console.log(response);
  const name = author.name;
  let birthday = author.birth_date;
  if (birthday === undefined) {
    birthday = "Unknown birth date";
  }

  //Card is created with HTML due to the large number of classes to be added. Show works button calls function with inputs specified in HTML.
  const cardHTML = `
    <div class="col-lg-3 col-md-6 mb-4">
    <div class="card m-auto">
      <img class="card-img-top" src="${imgURL}" alt="Card image cap" />
      <div class="card-body d-flex flex-column justify-content-between">
        <div>
          <h5 class="card-title mb-3">${name}</h5>
          <h6 class="card-subtitle text-muted mb-1">${birthday}</h6>
        </div>
          <a href="#" class="btn btn-primary" onclick="showWorks('${author.key}','${name}')">Show works</a>
      </div>
      </div>`;
  resultsContainer.insertAdjacentHTML("beforeend", cardHTML);
}
