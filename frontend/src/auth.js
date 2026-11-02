import { getMe } from "./api.js";

let currentUser = null;

const listeners = new Set();

export function getCurrentUser() {
    return currentUser;
}

export function setCurrentUser(user) {
    currentUser = user;
    listeners.forEach(listener => listener(user));
}

// Lets other parts of the app know when the user changes
export function subscribeToUser(listener) {
    listeners.add(listener);
}

export async function refreshUser() {
    setCurrentUser(await getMe());
}
