import React from 'react'

function Cancel() {
    return (
        <div className="p-10 text-center">
          <h1 className="text-3xl font-bold mb-4 text-red-600">
            ❌ Pagamento annullato
          </h1>
          <p className="mb-4">
            Il pagamento non è stato completato. Puoi riprovare a ordinare.
          </p>
          <button
            onClick={() => (window.location.href = "/carrello")}
            className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 transition"
          >
            Torna al carrello
          </button>
        </div>
      );
}

export default Cancel
