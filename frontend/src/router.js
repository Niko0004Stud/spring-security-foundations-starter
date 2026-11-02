import Navigo from "navigo";

const app = document.querySelector("#app");

let currentPage = null;

export function render(Page, props = {}) {
    // 1. Create new page
    const result = Page(props);

    // 2. Normalize page to support both DOM elements
    //    and objects with lifecycle methods
    const nextPage = result instanceof Node
        ? { element: result }
        : result;

    // 3. Validate that the page contains a DOM element
    if (!(nextPage?.element instanceof Node)) {
        throw new Error("Page must return a DOM node or page object");
    }

    // 4. Clean up previous page
    currentPage?.unmount?.();

    // 5. Replace previous page with new page
    app.replaceChildren(nextPage.element);

    // 6. Track current page
    currentPage = nextPage;

    // 7. Run lifecycle method after mounting
    nextPage.mount?.();
}

export const router = new Navigo("/");
