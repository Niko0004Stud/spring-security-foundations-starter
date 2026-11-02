import { refreshUser } from "./auth.js";
import { initHeader } from "./components/Header.js";
import { requireAuth } from "./guards.js";
import { NotFoundPage, ServerDownPage } from "./pages/ErrorPages.js";
import { HomePage } from "./pages/HomePage.js";
import { LoginPage } from "./pages/LoginPage.js";
import { NotesPage } from "./pages/NotesPage.js";
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
        .on("/notes", requireAuth(NotesPage))
        .notFound(() => render(NotFoundPage))
        .resolve();
}

init();
