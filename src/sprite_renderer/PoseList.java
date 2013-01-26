package sprite_renderer;

import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 * A PoseList is a linked list of Poses for an animated sprite. Such a list
 * would be used to represent the poses for a particular animated sprite state.
 * So, to render the sprite, just go through the pose list rendering each one in
 * order and then starting all over again, circularly looping through the poses.
 *
 * @author Richard McKenna Debugging Enterprises
 * @version 1.0
 */
public class PoseList {
    // THE FIRST POSE IN THE LIST

    private PoseNode headPose;
    // THE LAST POSE IN THE LIST
    private PoseNode tailPose;
    // LIST COUNTER
    private int numPoses;

    public int getNumPoses() {
        return numPoses;
    }

    public void setNumPoses(int numPoses) {
        this.numPoses = numPoses;
    }
    

    /**
     * This default (and only) constructor simply initializes and empty list.
     */
    public PoseList() {
        // THIS STARTS OUT AS AN EMPTY LIST
        headPose = null;
        tailPose = null;
        numPoses = 0;
    }

    /**
     * This method adds a new pose to this list. The pose will have the imageID
     * and durationInFrames arguments as its state.
     *
     * @param imageID The image to use for the pose being added.
     *
     * @param durationInFrames The number of frames to keep the pose being added
     * on the screen.
     */
    public void addPose(int imageID, int durationInFrames) {
        // CREATE THE NEW POSE
        Pose poseToAdd = new Pose(imageID, durationInFrames);

        // AND NOW ADD IT TO OUR SINGLY LINKED LIST
        if (headPose == null) {
            // HERE IT'S THE FIRST POSE IN THE LIST
            headPose = new PoseNode(poseToAdd, null);
            tailPose = headPose;
        } else {
            // AND HERE WE'RE JUST APPENDING IT ONTO THE END
            tailPose.next = new PoseNode(poseToAdd, null);
            tailPose = tailPose.next;
        }
        // DON'T FORGET TO INCREMENT THE POSE COUNTER
        numPoses++;
    }

    public void deletePose(Pose poseToDelete) {

        if (headPose.pose == poseToDelete) {
            headPose = headPose.next;
        } else {
            PoseNode pre = headPose;
            PoseNode temp = headPose.next;
            while (temp.pose != poseToDelete) {
                pre = temp;
                temp = temp.next;
            }
            pre.next = temp.next;
            if (pre.next == null) {
                tailPose = pre;
            }
        }
        numPoses--;
    }

    public boolean movePoseUp(Pose poseToMoveUp) {
        if (poseToMoveUp != headPose.pose) {
            if (poseToMoveUp == headPose.next.pose) {
                PoseNode current = headPose.next;
                if (numPoses == 2) {
                    tailPose = headPose;
                    current.next = tailPose;
                    headPose = current;
                } else {
                    headPose.next = current.next;
                    current.next = headPose;
                    headPose = current;
                }
            } else if (poseToMoveUp == headPose.next.pose) {
                PoseNode current = headPose.next;
                headPose.next = current.next;
                current.next = headPose;
                headPose = current;
            } else if (poseToMoveUp == tailPose.pose) {
                PoseNode pre = headPose;
                PoseNode before = headPose.next;
                while (before.next != tailPose) {
                    pre = before;
                    before = before.next;
                }
                pre.next = tailPose;
                tailPose.next = before;
                tailPose = before;
            } else {
                PoseNode prePre = headPose;
                PoseNode pre = headPose.next;
                PoseNode current = headPose.next.next;
                while (current.pose != poseToMoveUp) {
                    prePre = pre;
                    pre = current;
                    current = current.next;
                }
                prePre.next = current;
                pre.next = current.next;
                current.next = pre;
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "The pose is already on top.");
            return false;
        }
    }

    public boolean movePoseDown(Pose poseToMoveDown) {
        if (poseToMoveDown != tailPose.pose) {
            if (numPoses == 2) {
                PoseNode temp = tailPose;
                tailPose.next = headPose;
                tailPose = headPose;
                headPose = temp;
            } else {
                if(poseToMoveDown == headPose.pose) {
                    PoseNode third = headPose.next.next;
                    PoseNode second = headPose.next;
                    headPose.next = third;
                    second.next = headPose;
                    headPose = second;
                } else {
                    PoseNode current = headPose;
                    PoseNode pre = headPose;
                    while(current.pose != poseToMoveDown) {
                        pre = current;
                        current = current.next;
                    }
                    PoseNode temp = current.next;
                    if(temp==tailPose) {
                        tailPose = current;
                    }
                    else {
                        current.next=temp.next;
                    }
                    pre.next = temp;
                    temp.next = current;                    
                }
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "The pose is already on the bottom.");
            return false;
        }
    }

    /**
     * This accessor method returns a circular iterator to be used to go through
     * all the poses repeatedly.
     *
     * @return A circular Iterator that will start at the first pose in the list
     * and will produce elements circularly forever.
     */
    public Iterator<Pose> getPoseIterator() {
        // RETURN OUR OWN DEFINED TYPE OF ITERATOR
        return new CircularPoseIterator();
    }
    
    public Iterator<Pose> getPoseEndIterator() {
        return new LinearPoseIterator();
    }

    private class LinearPoseIterator implements Iterator {
        // THIS VARIABLE KEEPS TRACK OF WHICH POSE THE
        // ITERATOR SHOULD PRODUCE NEXT

        private PoseNode currentPose;

        // THIS DEFAULT CONSTRUCTOR STARTS THE ITERATOR
        // AT THE FIRST POSE IN THE POSE LIST
        public LinearPoseIterator() {
            currentPose = headPose;
        }

        // SINCE THE LIST IS CIRCULAR, AS LONG AS THERE ARE
        // ELEMENTS IN THE LIST, THERE WILL BE MORE TO PRODUCE
        @Override
        public boolean hasNext() {
            return (currentPose != null);
        }

        // PRODUCE THE NEXT ELEMENT IN THE LIST, AND THEN MOVE
        // THE MARKER TO THE NEXT ELEMENT, WHICH MAY BE THE
        // FIRST ONE
        @Override
        public Pose next() {
            Pose poseToReturn = currentPose.pose;
            if (currentPose == tailPose) {
                currentPose = null;
            } else {
                currentPose = currentPose.next;
            }
            return poseToReturn;
        }

        // WE WON'T USE THIS METHOD
        @Override
        public void remove() {
        }
    }

    // This private class will serve as the iterator for
    // producing all the poses circularly in sequenctial
    // order.
    //
    // WE'LL CONCEAL THIS private CLASS FOR API DOCUMENTATION
    // PURPOSES.
    private class CircularPoseIterator implements Iterator {
        // THIS VARIABLE KEEPS TRACK OF WHICH POSE THE
        // ITERATOR SHOULD PRODUCE NEXT

        private PoseNode currentPose;

        // THIS DEFAULT CONSTRUCTOR STARTS THE ITERATOR
        // AT THE FIRST POSE IN THE POSE LIST
        public CircularPoseIterator() {
            currentPose = headPose;
        }

        // SINCE THE LIST IS CIRCULAR, AS LONG AS THERE ARE
        // ELEMENTS IN THE LIST, THERE WILL BE MORE TO PRODUCE
        @Override
        public boolean hasNext() {
            return (currentPose != null);
        }

        // PRODUCE THE NEXT ELEMENT IN THE LIST, AND THEN MOVE
        // THE MARKER TO THE NEXT ELEMENT, WHICH MAY BE THE
        // FIRST ONE
        @Override
        public Pose next() {
            Pose poseToReturn = currentPose.pose;
            if (currentPose == tailPose) {
                currentPose = headPose;
            } else {
                currentPose = currentPose.next;
            }
            return poseToReturn;
        }

        // WE WON'T USE THIS METHOD
        @Override
        public void remove() {
        }
    }

    // This node class is used by the list. Note that
    // we make the instance variables public for convenience,
    // which is ok because these objects are never used
    // outside of the PoseList class.
    private class PoseNode {

        public Pose pose;
        public PoseNode next;

        public PoseNode(Pose initPose, PoseNode initNext) {
            pose = initPose;
            next = initNext;
        }
    }
}
