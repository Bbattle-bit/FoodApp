import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

function Home_admin() {
    const navigate = useNavigate();

    // Controllo del token JWT e del ruolo dell'utente
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/LogIn");
            return;
        }
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            if (payload.role !== "ADMIN") {
                navigate("/LogIn");
            }
        } catch (err) {
            console.error("Token JWT non valido:", err);
            navigate("/LogIn");
        }
    }, [navigate]);

    return (
        <div id="right-navbar" className="">
            <h1 className="text-center">Benvenuto Admin!</h1>
            <ul>
                <li><a href="/admin/gestione_menu">Gestione Menu</a></li>
                <li><a href="/admin/gestione_ordini">Gestione Ordini</a></li>
                <li><a href="/admin/gestione_utenti">Gestione Utenti</a></li>
            </ul>
        </div>
    );
}

export default Home_admin