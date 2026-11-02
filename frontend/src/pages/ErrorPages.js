function errorPage(title, text) {
    const section = document.createElement("section");

    section.innerHTML = `
        <h1>${title}</h1>
        <p>${text}</p>
    `;

    return section;
}

export function NotFoundPage() {
    return errorPage("404: Page not found", "The page you're looking for doesn't exist.");
}

export function ForbiddenPage() {
    return errorPage("403: Forbidden", "You do not have permission to access this page.");
}

export function ServerDownPage() {
    return errorPage("Can't reach the server", "GET /api/me failed. Is Spring running on port 8080?");
}
