import { refreshUser } from "./auth.js";
import { initHeader } from "./components/Header.js";
import { NotFoundPage, ServerDownPage } from "./pages/ErrorPages.js";
import { HomePage } from "./pages/HomePage.js";
import { LoginPage } from "./pages/LoginPage.js";
import { render, router } from "./router.js";

async function init() {
    initHeader();

    try {
        await refreshUser();
    } catch (error) {
        console.error(error);
        render(ServerDownPage);
        return;
    }

    router
        .on("/", () => render(HomePage))
        .on("/login", () => render(LoginPage))
        .notFound(() => render(NotFoundPage))
        .resolve();
}

init();
