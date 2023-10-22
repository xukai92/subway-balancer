from matplotlib import pyplot as plt

def subplots(*args, **kwargs):
    retval = plt.subplots(*args, **kwargs)
    plt.close(retval[0])
    return retval

def make_carriage_fig(data_dir):
    fig, axes = subplots(1, 5, figsize=(16, 5))

    for (i, ax) in enumerate(axes):
        img = plt.imread(f"{data_dir}/subway-{i+1}.jpeg")
        ax.imshow(img)

    return fig

def read_level_lst(fn):

    with open(fn, 'r') as file:
        content = file.read()

    count_lst = [int(n) for n in content.strip().split(',')]

    # 5 - 10 - 30
    level_lst = ["empty" if c <= 5 else "mild" if c <= 10 else "moderate" if c <= 30 else "crowded" for c in count_lst]

    return level_lst

DIRECTION_MAP = {
    "None": None,
    "Some(Right)": "right",
    "Some(Left)": "left",
}

def read_direction_lst(fn):
    with open(fn, 'r') as file:
        content = file.read()

    raw_direction_lst = [s.strip() for s in content.strip().split(',')]

    direction_lst = [DIRECTION_MAP[rd] for rd in raw_direction_lst]

    return direction_lst

COLOR_MAP = {
    "empty": "white",
    "mild": "yellow",
    "moderate": "orange",
    "crowded": "red",
}

def vis(level_lst, direction_lst):
    x_loc_lst = [550, 850, 1175, 1475, 1800]

    fig, ax = subplots(figsize=(16, 3.5))

    img = plt.imread(f"train.png")
    ax.imshow(img)

    for (x_loc, level, direction) in zip(x_loc_lst, level_lst, direction_lst):
        ax.annotate(level, xy=(2, 1), xytext=(x_loc, 100), backgroundcolor=COLOR_MAP[level])
        if direction == "left":
            ax.annotate("", xy=(x_loc - 40, 120), xytext=(x_loc+110, 120), arrowprops=dict(arrowstyle="->"))
        if direction == "right":
            ax.annotate("", xy=(x_loc + 140, 120), xytext=(x_loc-10, 120), arrowprops=dict(arrowstyle="->"))

    return fig