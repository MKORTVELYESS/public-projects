function getPostText(post) {
  const endChar =
    post.text.indexOf("Fordítás megtekintése") === -1
      ? post.text.length
      : post.text.indexOf("Fordítás megtekintése");
  const text = post.text;
  const content = text.substring(0, endChar);

  if (post._attrs.role == "article") {
    const cleanContent = content
      .replaceAll("Facebook", "")
      .replaceAll("Láthatóság: Az Eladó színházjegy csoport tagjai", "");

    return cleanContent;
  } else {
    const authorText = removeWhiteSpace(getAuthor(post).text);
    const postText = removeWhiteSpace(content).replaceAll(
      "Láthatóság: Az Eladó színházjegy csoport tagjai",
      ""
    );
    return authorText + " " + postText;
  }
}

function removeWhiteSpace(string) {
  return string.replace(/\s+/g, " ").trim().replace(";", ",");
}

function getAuthor(post) {
  if (post._attrs.role !== "article") {
    return post.parentNode.previousElementSibling;
  }
}

export default getPostText;
