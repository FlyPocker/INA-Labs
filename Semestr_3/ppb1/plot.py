import csv
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd


def read_results(path: Path) -> tuple[dict[str, str], pd.DataFrame]:
    metadata: dict[str, str] = {}
    with path.open(newline="") as file:
        while True:
            position = file.tell()
            line = file.readline()
            if not line or not line.startswith("# "):
                file.seek(position)
                break
            key, value = line[2:].strip().split(",", 1)
            metadata[key] = value

        rows = list(csv.DictReader(file))

    if not metadata:
        raise ValueError("results.csv does not contain metadata.")
    required = {"function", "a", "b", "M", "exact_integral", "repetitions"}
    missing = required - metadata.keys()
    if missing:
        raise ValueError(f"Missing metadata: {', '.join(sorted(missing))}")

    return metadata, pd.DataFrame(rows).astype({"n": int, "result": float})


metadata, data = read_results(Path("results.csv"))
exact_value = float(metadata["exact_integral"])
averages = data.groupby("n", as_index=False)["result"].mean()

plt.figure(figsize=(10, 6))
plt.scatter(
    data["n"],
    data["result"],
    color="blue",
    s=5,
    alpha=0.6,
    label="Individual repetitions",
)
plt.scatter(
    averages["n"],
    averages["result"],
    color="red",
    s=20,
    zorder=5,
    label="Mean",
)
plt.axhline(
    y=exact_value,
    color="green",
    linestyle="-",
    linewidth=2,
    label=f"Exact value ({exact_value:.12g})",
)

plt.xlabel("Number of random points (n)")
plt.ylabel("Estimated integral")
plt.title(
    f"Monte Carlo integration: {metadata['function']} "
    f"[{metadata['a']}, {metadata['b']}], "
    f"k={metadata['repetitions']}"
)
plt.legend()
plt.grid(True, linestyle="--", alpha=0.5)
plt.tight_layout()
output_path = f"{metadata['function']}_{metadata['repetitions']}.png"
plt.savefig(output_path, dpi=300)
plt.show()
