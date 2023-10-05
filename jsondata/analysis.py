import json
import numpy as np

if __name__ == "__main__":
    l = []
    with open("listingsAndReviews.json", "r") as file:
        for line in file:
            l.append(len(line))
    print("Sample Data Stats")
    print("Disk size 95MB ")
    print(
        "# documents: {}, mean(B): {:.0f} ,min(B): {} ,max(B): {}, median(B): {}, std: {:.0f}".format(
            len(l), np.mean(l), np.min(l), np.max(l), np.median(l), np.std(l)
        )
    )
    print("*" * 50)
    print(l)
