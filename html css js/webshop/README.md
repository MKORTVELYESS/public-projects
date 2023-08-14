# Webshop

### JS

#### shopData

This is a hardcopy of the data that can be fetched from the api.

#### lgBreakpoint

Defines a variable that is the breakpoint used in the code for changing the navigation bar into a hamburger menu.

#### printCheaperThan

Demonstrates how .filter can be used to find all items that are cheaper than a given price. This is only a logic that has no effect in how the document is rendered.

#### getCheapestProduct

Demonstrates how .reduce can be used to find the cheapest price from an array of JS objects. This is only a logic that has no effect in how the document is rendered.

#### render

Creates the elements in the DOM representing the articles in the webshop. It get the products to be rendered as an input.

#### getUniqueCategories

Iterates all categories and creates a unique array of the available categories. The unique array is used later in creating the navigation bar.

#### drawCategories2

- The function gets the output of getUniqueCategories as an input.
- It creates a button for each category in the DOM.
- The buttons listen to user interaction and change background color when active.
- The active status can be removed by clicking the button again or clicking another button.
- In case of a click event the articles in the corresponding category will appear on the webpage.
- The function reuses the previously declared render function.

#### toggleCollapseAnimation

Turns on the collapse animation below the breakpoint specified in variable lgBreakpoint. There is no collapse animation if the viewport is wider than the breakpoint.

#### renderFromAPI

Async operation that calls the previously defines function with input data retrieved from an API on url https://fakestoreapi.com/products rather than from the JS file variable

### CSS

- All images are forced to the same width
- .products-container is defining the dynamic grid system that will respond to the user changing the viewport width. The number of articles displayed in a row will decrease automatically as the viewport width decreases.
- .category-button text-transform changes the rendered text of the navigation bar elements into capitalize

### HTML

Linkin own stylesheet and bootstrap stylesheets in link tag.
Linking own script and bootstrap scripts in script tag. Bootstrap scripts are required for the hamburger menu behavior.

#### ul class="navbar-nav ml-auto"

Used to accomodate navigation bar buttons dynamically populated from js.

#### div class="products-container"

Used to accomoated articles of the store populated dynamically from js.
