export function HomePage() {
    const section = document.createElement("section");

    section.innerHTML = `
        <h1>Welcome</h1>
        <p>Your notes, protected by Spring Security.</p>
        <a href="/notes" data-navigo>Go to your notes</a>
    `;

    return section;
}
