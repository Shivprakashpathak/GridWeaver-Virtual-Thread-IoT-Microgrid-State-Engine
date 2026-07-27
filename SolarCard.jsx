function SolarCard({ data }) {
  return (
    <div className="card">
      <h2>☀ Solar</h2>
      <h3>{data.power}</h3>
      <p>Generation: {data.generation}</p>
    </div>
  );
}

export default SolarCard;