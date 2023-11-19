function getPostText(post) {
  const endChar =
    post.text.indexOf("Fordítás megtekintése") === -1
      ? post.text.length
      : post.text.indexOf("Fordítás megtekintése");
  const text = post.text;
  const content = text.substring(0, endChar);

  const cleanContent = removeWhiteSpace(
    content
      .replaceAll("Facebook", "")
      .replaceAll("Láthatóság: Az Eladó színházjegy csoport tagjai", "")
  );

  return cleanContent;
}

function removeWhiteSpace(string) {
  return string.replace(/\s+/g, " ").trim().replaceAll(";", ",");
}

// function getAuthor(post) {
//   if (post._attrs.role !== "article") {
//     return post.parentNode.previousElementSibling;
//   }
// }

export default getPostText;
