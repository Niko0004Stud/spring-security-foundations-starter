import { getCurrentUser, setCurrentUser } from "./auth.js";
import { ForbiddenPage } from "./pages/ErrorPages.js";
import { render, router } from "./router.js";

// Guards only decide what the user sees. Anyone can change the JavaScript in
// their own browser, so Spring Security must still protect every endpoint.

// Replace the guarded URL, so the Back button doesn't lead straight back to the redirect
const REDIRECT = { historyAPIMethod: "replaceState" };

export function requireAuth(Page) {
    return () => {
        if (!getCurrentUser()) {
            router.navigate("/login", REDIRECT);
            return;
        }

        render(Page);
    };
}

export function requireRole(Page, role) {
    return () => {
        const user = getCurrentUser();

        if (!user) {
            router.navigate("/login", REDIRECT);
            return;
        }

        if (!user.roles.includes(role)) {
            render(ForbiddenPage);
            return;
        }

        render(Page);
    };
}

export function requireGuest(Page) {
    return () => {
        if (getCurrentUser()) {
            router.navigate("/", REDIRECT);
            return;
        }

        render(Page);
    };
}

// A 401 from the API means the session is gone, for example after a server restart
export function handleAuthError(error) {
    if (error.status !== 401) {
        return false;
    }

    setCurrentUser(null);
    router.navigate("/login", REDIRECT);
    return true;
}
