# Find works of authors web app

This application demonstrates fetching data from multiple api endpoints and displaying them to the user using bootstrap and custom css. The display responds to the screen sizing.

## Requirements:

- Using API: https://openlibrary.org/ (documentation: https://openlibrary.org/developers/api)
- User inputs the name of an author into the form.
- The input on click of the submit button send a request to the API `https://openlibrary.org/dev/docs/api/authors`
- Query parameter url as follows: `https://openlibrary.org/search/authors.json?q=<author>`
- When the API responds .cards are created and positioned into columns and rows using the bootstrap grid.
- The cards contain the name, birthdate and photo (where available) of the authors. - the author api respond contains the image `https://covers.openlibrary.org/a/olid/<key>-M.jpg` where Key is the unique key of the author included in the first API request
- Show works button is included on the cards. This send another request to endpoint `https://openlibrary.org/authors/<key>/works.json`.
- The title of the books that correspond to the author will be displayed in a modal.
- Tha modal can be closed with the close button in the bottom right or the 'X' in the top right.
