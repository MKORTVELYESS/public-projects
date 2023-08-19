const inputSubmit = document.querySelector(
  '.author-search input[type="submit"]'
);
const inputAuthor = document.getElementById("input-author-name");

//Form submission needs to be prevented when submit button is clicked
document.addEventListener("DOMContentLoaded", function () {
  const searchForm = document.getElementsByTagName("form")[0];
  searchForm.addEventListener("submit", function (event) {
    event.preventDefault();
  });
});

async function getAuthorsList(authorName) {
  const url = `https://openlibrary.org/search/authors.json?q=${authorName}`;
  let response = await fetch(url);
  response = await response.json();
  return response.docs;
}

inputSubmit.addEventListener("click", async function () {
  const authorName = inputAuthor.value;
  const authorsArr = await getAuthorsList(authorName);
  renderAuthorCard(author);
});
