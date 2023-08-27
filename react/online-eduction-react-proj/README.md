# Modular website demonstraing the use of React + Vite function components

- This code shows the modular structure of a website created with react. (files in "./src")
- React functional components are used where necessary, the project is broker into several .jsx files.
- The .jsx files use import and export prompts to be bound together into one application.
- The EduApp.jsx file consolidates all modules and is used in main.jsx.
- index.html is displaying the main.jsx project.
- The react project can be viewed using npm run dev command in the terminal and should start up a localhost for the page.
- The CARDS_PROPS constant dynamically declares the cards that are inserted into the DOM using {...props} to deconstruct the props object and make it visible in Card.jsx.
- .map is used to create the cards using loop on CARDS_PROPS constant in CardsContainer.jsx. A unique key is set for each element in the list using key= property.
- PrimaryButton.jsx components are set using buttonProps const-s defined in the parents. (LoginForm.jsx and TextContainer.jsx)

# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh
