import * as help from "./helpers.js";
export const $PROMPT =
  "Készíts egy táblázatos formájú összefoglalót a következő színházjegy eleadási ajánlatokból. A következő oszlopokat töltsd ki pontosan ebben a sorrendben: Eladó neve, Színházi előadás címe, Színház neve, Előadás dátuma, Sor száma, Előadás kezdete (óra), Eladó jegyek száma, Egy darab jegy ára, Kezelési költség jegyenként. Előadás dátumonként egy sor jelenljen meg. Abban az esetben ha az adott cella értéke ismeretlen írd az 'n/a' kifejezést a cellába. Amennyiben több dátum elérhető akkor dátumonként kezdj új sort ahova beírod a az adott dátumhoz tartozó információt. A mai dátum " +
  help.getDate() +
  ". Az előadás dátuma minden esetben a mai dátumnál későbbi kell legyen. Az üzenetek pontosvesszővel elválasztva a következők: ";
export const $LIMIT = 30;
export const $FEED_FILEPATH = "./input/feed.html";
export const $POST_SELECTOR = "div[role='article']";
export const $LAST_FILEPATH = "./input/lastPost.txt";
export const $OUTPUT_FILEPATH = "./output/result";
export const $ARTICLE_AUTHOR_SELECTOR =
  "div > div > div > div > div > div:nth-child(8) > div > div > div:nth-child(2)";
export const $ARTICLE_CONTENT_SELECTOR =
  "div > div > div > div > div > div:nth-child(8) > div > div > div:nth-child(3)";
export const $RESULT_FOLDER = "./output";
//   div[data-ad-comet-preview='message'],
