import { NotFoundPage } from "./pages/ErrorPages.js";
import { HomePage } from "./pages/HomePage.js";
import { render, router } from "./router.js";

router
    .on("/", () => render(HomePage))
    .notFound(() => render(NotFoundPage))
    .resolve();
