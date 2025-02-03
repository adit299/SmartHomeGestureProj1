from flask import Flask, request

app = Flask(__name__)

# A dictionary to keep track of how many of each gesture I have recorded so far
gestureCounts = {}

@app.route("/", methods=['GET', 'POST'])
def upload_video():
    file = request.files['file']
    savedFilename = parseFilename(file.filename)
    file.save(savedFilename)
    print("Successfully in the post request...")
    return "Video uploaded successfully", 200


def parseFilename(fileName):
    # Split the name on _
    lst = fileName.split("_")

    # Add to the dictionary to keep track of how many gestures I've recorded so far
    if lst[0] not in gestureCounts:
        gestureCounts[lst[0]] = 1
    else:
        gestureCounts[lst[0]] += 1

    # Add this to the lst
    lst[2] = str(gestureCounts[lst[0]])

    # Put the name back together     
    return '_'.join(lst)


