import { refreshUser } from "./auth.js";
import { NotFoundPage, ServerDownPage } from "./pages/ErrorPages.js";
import { HomePage } from "./pages/HomePage.js";
import { render, router } from "./router.js";

async function init() {
    try {
        await refreshUser();
    } catch (error) {
        console.error(error);
        render(ServerDownPage);
        return;
    }

    router
        .on("/", () => render(HomePage))
        .notFound(() => render(NotFoundPage))
        .resolve();
}

init();
