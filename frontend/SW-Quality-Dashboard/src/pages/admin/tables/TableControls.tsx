import React from "react";

export default function TableControls({setSearchTerm, setItemsPerPage}) {
    return (
        <>
            <div className="card bg-base-content/10"
                 style={{
                     padding: "2vh",
                     marginTop: "2vh",
                     display: "flex",
                     flexDirection: "row",
                     gap: "2vh",
                     justifyContent: "space-between",
                     alignItems: "center"
                 }}
            >
                <label className="form-control" style={{width: "85%"}}>
                    <div className="label">
                        <span className="label-text"><strong>
                            <i className="bi bi-search"> </i>
                            Search
                        </strong></span>
                    </div>
                    <input style={{width: "100%"}} type="text" placeholder="Type here" className="input input-bordered input-xs" onChange={(e) => setSearchTerm(e.target.value)} />
                </label>
                <label className="form-control" style={{width: "auto"}}>
                    <div className="label">
                        <span className="label-text"><strong>
                            <i className="bi bi-filter"> </i>
                            Items per Page
                        </strong></span>
                    </div>
                    <select className="select select-bordered select-xs"  onChange={(e) => setItemsPerPage(parseInt(e.target.value))}>
                        <option disabled>Page Size</option>
                        <option value={5}>5</option>
                        <option value={10}>10</option>
                        <option value={15}>15</option>
                        <option value={20}>20</option>
                        <option value={25}>25</option>
                        <option value={30}>30</option>
                        <option value={35}>35</option>
                        <option value={40}>40</option>
                        <option value={45}>45</option>
                        <option value={50}>50</option>
                    </select>
                </label>

                <label className="form-control tooltip tooltip-bottom" style={{width: "2%"}} data-tip={"Refresh table"}>
                    <button className="btn btn-primary btn-xs" onClick={() => window.location.reload()}>
                        <i className="bi bi-arrow-clockwise"> </i>
                    </button>
                </label>
            </div>
        </>
    )
}