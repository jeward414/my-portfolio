 
// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

public final class FindMeetingQuery {

    private final int endOfDay = TimeRange.END_OF_DAY;

    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        // Collects data from the request: meeting duration and the attendees and optional attendees
        int duration = (int) request.getDuration();
        Collection<String> attendees = request.getAttendees();
        Collection<String> opAttendees = request.getOptionalAttendees();


        // This is the collection of time ranges that we will return that fits everyones schedule
        Collection<TimeRange> unavailableTimes = new ArrayList<>();
        Collection<TimeRange> availableTimes = new ArrayList<>();

        int startTime = 0;
        int endTime = startTime + duration;


        // Finds all unavailable times
        while( startTime < endOfDay ) {
            TimeRange unavailableMeetTime = TimeRange.fromStartDuration(startTime, duration);
            
            for (Event event : events) {
                TimeRange eventMeetTime = event.getWhen();
                Collection<String> eventAttendees = event.getAttendees();

                if (eventMeetTime.overlaps(unavailableMeetTime)) {
                    if(!Collections.disjoint(eventAttendees, attendees)) {
                        unavailableTimes.add(unavailableMeetTime);
                    }
                } 

            }
            startTime = startTime + duration;
        }

        // Reset startTime for other tests
        startTime = 0;

        // Takes the end of every unavailable time and makes that the available times for meetings
        for (TimeRange time : unavailableTimes) {
            endTime = time.start();
            TimeRange overLap = TimeRange.fromStartEnd(startTime, endTime, false);
            if (overLap.duration() >= request.getDuration()) {
                availableTimes.add(overLap);
            }
            startTime = endTime + time.duration();
        }
        
        // Finds the very last time for possible meetings
        TimeRange lastTime = TimeRange.fromStartEnd(startTime, endOfDay, true);
        if (lastTime.duration() >= request.getDuration()) {
            availableTimes.add(lastTime);
        }

        return availableTimes;
    }
}