using DelimitedFiles, Comonicon

DARKNET_DIR="/Users/kai/src/nyc-ai-hackathon/darknet"

function parse_yolo(rawout)
    lines_with_elpased = split(rawout, "\n")
    lines = lines_with_elpased[2:end]
    return lines
end

@main function main(img_dir::String, num_images::Int)
    current_dir = pwd()
    cd(DARKNET_DIR)

    num_people_lst = zeros(Int, num_images)
    for i in 1:num_images
        cmd = `./darknet detector test cfg/coco.data cfg/yolov3.cfg yolov3.weights $img_dir/subway-$i.jpeg`
        rawout = read(cmd, String)
        lines = parse_yolo(rawout)
        lines_with_person = filter(line -> occursin("person", line), lines)
        num_people_lst[i] = length(lines_with_person)
    end

    open("$img_dir/subway-out.txt", "w") do io
        writedlm(io, num_people_lst', ',')
    end;

    cd(current_dir)
end